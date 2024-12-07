import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './screen.reducer';

export const ScreenDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const screenEntity = useAppSelector(state => state.screen.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="screenDetailsHeading">
          <Translate contentKey="larissAsstApp.screen.detail.title">Screen</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{screenEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="larissAsstApp.screen.name">Name</Translate>
            </span>
          </dt>
          <dd>{screenEntity.name}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="larissAsstApp.screen.value">Value</Translate>
            </span>
          </dt>
          <dd>{screenEntity.value}</dd>
        </dl>
        <Button tag={Link} to="/screen" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/screen/${screenEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ScreenDetail;
